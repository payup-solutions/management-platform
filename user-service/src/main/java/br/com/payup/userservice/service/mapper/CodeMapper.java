package br.com.payup.userservice.service.mapper;

import br.com.payup.userservice.domain.*;
import br.com.payup.userservice.service.dto.CodeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Code and its DTO CodeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CodeMapper extends EntityMapper <CodeDTO, Code> {

    CodeDTO toDto(Code code); 

    Code toEntity(CodeDTO codeDTO); 
    default Code fromId(Long id) {
        if (id == null) {
            return null;
        }
        Code code = new Code();
        code.setId(id);
        return code;
    }
}
