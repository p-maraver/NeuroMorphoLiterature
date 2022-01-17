/*
 * Copyright (c) 2015-2022, Patricia Maraver
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */


package org.neuromorpho.literature.search.dto.fulltext.pubmed;


import org.neuromorpho.literature.search.dto.fulltext.AuthorDto;
import org.neuromorpho.literature.search.dto.fulltext.AuthorGroupDto;
import org.neuromorpho.literature.search.service.pubmed.model.fulltext.Author;
import org.neuromorpho.literature.search.service.pubmed.model.fulltext.AuthorList;

import java.util.ArrayList;
import java.util.List;


public class AuthorGroupDtoAssembler {


    protected AuthorGroupDto createAuthorGroupDto(AuthorList authorList) {
        AuthorGroupDto authorGroupDto = new AuthorGroupDto();
        authorGroupDto.setAffiliationList(authorList.getAffiliationList());
        authorGroupDto.setAuthorList(this.createAuthorListDto(authorList.getAuthorList()));
        return authorGroupDto;
    }

    private List<AuthorDto> createAuthorListDto(List<Author> authorList) {
        List<AuthorDto> authorDtoList = null;
        try {
            authorDtoList = new ArrayList();
            for (Author author : authorList) {
                AuthorDto authorDto = new AuthorDto();
                authorDto.setGivenName(author.getGivenName());
                authorDto.setSurname(author.getSurname());
                authorDtoList.add(authorDto);
            }
        } catch (NullPointerException ex) {
        }
        return authorDtoList;


    }


}
