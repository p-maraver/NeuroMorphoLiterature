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


package org.neuromorpho.literature.search.dto.fulltext.jats;


import org.neuromorpho.literature.search.dto.fulltext.AuthorDto;
import org.neuromorpho.literature.search.dto.fulltext.AuthorGroupDto;
import org.neuromorpho.literature.search.service.jats.model.fulltext.Contributor;
import org.neuromorpho.literature.search.service.jats.model.fulltext.ContributorGroup;

import java.util.ArrayList;
import java.util.List;


public class AuthorGroupDtoAssembler {


    protected AuthorGroupDto createAuthorGroupDto(List<ContributorGroup> contributorGroupList, List<String> affiliation) {
        AuthorGroupDto authorGroupDto = new AuthorGroupDto();
        authorGroupDto.setAffiliationList(affiliation);
        authorGroupDto.setAuthorList(this.createAuthorListDto(contributorGroupList.get(0).getContributorList()));

        return authorGroupDto;
    }

    private List<AuthorDto> createAuthorListDto(List<Contributor> authorList) {
        List<AuthorDto> authorDtoList = null;
        try {
            authorDtoList = new ArrayList();
            for (Contributor author : authorList) {
                if (author.isAuthor()) {
                    AuthorDto authorDto = new AuthorDto();
                    authorDto.setEmail(author.getEmail());
                    authorDto.setGivenName(author.getGivenName());
                    authorDto.setSurname(author.getSurname());
                    authorDtoList.add(authorDto);
                }
            }
        } catch (NullPointerException ex) {
        }
        return authorDtoList;


    }


}
