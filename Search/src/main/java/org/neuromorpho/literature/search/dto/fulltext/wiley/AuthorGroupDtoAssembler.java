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

package org.neuromorpho.literature.search.dto.fulltext.wiley;


import org.neuromorpho.literature.search.dto.fulltext.AuthorDto;
import org.neuromorpho.literature.search.dto.fulltext.AuthorGroupDto;
import org.neuromorpho.literature.search.service.wiley.model.fulltext.Creator;

import java.util.ArrayList;
import java.util.List;


public class AuthorGroupDtoAssembler {


    protected AuthorGroupDto createAuthorGroupDto(List<Creator> creatorList) {
        AuthorGroupDto authorGroupDto = new AuthorGroupDto();
        authorGroupDto.setAuthorList(this.createAuthorListDto(creatorList));

        return authorGroupDto;
    }

    private List<AuthorDto> createAuthorListDto(List<Creator> creatorList) {
        List<AuthorDto> authorDtoList = null;
        try {
            authorDtoList = new ArrayList();
            for (Creator author : creatorList) {
                AuthorDto authorDto = new AuthorDto();
                authorDto.setEmail(author.getEmail());
                authorDto.setGivenName(author.getGivenName());
                authorDto.setSurname(author.getSurname());
                authorDtoList.add(authorDto);
            }
        } catch (NullPointerException ex) {
        }
        return authorDtoList;


    }


}
