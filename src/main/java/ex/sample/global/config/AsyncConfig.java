package ex.sample.global.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    private static final int CPU_CORE_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 범용적인 I/O 작업의 경우 가상 스레드 활용
     */
    @Bean
    @Primary
    public Executor taskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * CPU 집약적인 작업의 경우 플랫폼 스레드풀 활용
     */
    @Bean // 스프링 컨테이너 종료 시 ThreadPoolTaskExecutor 자체적인 shutdown 메서드 실행
    @Lazy // 스레드당 2MB의 메모리를 점유하므로, 실제 이용 전까지 생성 지연
    public ThreadPoolTaskExecutor cpuTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 기본 스레드 풀 설정
        executor.setCorePoolSize(CPU_CORE_COUNT); // 활성 스레드 수가 CorePoolSize 미만이면 스레드를 생성하여 작업 할당
        executor.setQueueCapacity(2 * CPU_CORE_COUNT); // 이상이면 QueueCapacity 만큼 저장되어 대기
        executor.setMaxPoolSize(CPU_CORE_COUNT); // 큐가 가득 차면, maxPoolSize 만큼 스레드 생성하여 할당
        executor.setRejectedExecutionHandler(new CallerRunsPolicy()); // maxPoolSize 초과하면 CallerRunsPolicy 정책에 따라 요청 스레드가 직접 작업 처리

        // 생명주기 관리
        executor.setWaitForTasksToCompleteOnShutdown(true); // 종료 시 진행 중인 작업은 완료/지정시간 까지 대기
        executor.setAwaitTerminationSeconds(30); // 종료 시 최대 30초까지 완료 대기 후 강제 종료
        executor.setKeepAliveSeconds(60); // CorePoolSize 초과 상태에서 대기 상태인 스레드를 종료시키는 대기 시간

        executor.setThreadNamePrefix("CpuTaskExecutor-"); // 로깅 시 접두사 (이후 번호 붙음)
        executor.initialize(); // 위 설정을 바탕으로 스레드풀 생성 및 설정

        return executor;
    }
}
