package ex.sample.domain.sample.mapper;

import ex.sample.domain.sample.domain.Sample;
import ex.sample.domain.sample.dto.response.CreateSampleResponse;
import ex.sample.domain.sample.dto.response.GetSampleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SampleMapper {

    @Mapping(target = "money", source = "money.amount")
    GetSampleResponse toGetSampleRes(Sample sample);

    @Mapping(target = "money", source = "money.amount")
    CreateSampleResponse toCreateSampleRes(Sample sample);
}
