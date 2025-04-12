package ex.sample.domain.sample.mapper;

import ex.sample.domain.sample.domain.Sample;
import ex.sample.domain.sample.dto.response.CreateSampleRes;
import ex.sample.domain.sample.dto.response.GetSampleRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SampleMapper {

    @Mapping(target = "money", source = "money.amount")
    GetSampleRes toGetSampleRes(Sample sample);

    @Mapping(target = "money", source = "money.amount")
    CreateSampleRes toCreateSampleRes(Sample sample);
}
