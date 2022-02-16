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

import {AbstractControl, ValidatorFn} from '@angular/forms';
import {Collection} from '../model/collection';

export function fullNameEmail(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let fullName = true;
    const authorList = control.get('data.authorList').value;
    if (control.get('status.articleStatus').value === Collection.Positive) {
      if (control !== undefined && authorList != null && authorList.length > 0) {
        const lastAuthor = authorList[authorList.length - 1];
        const fullLastAuthorNameList = lastAuthor.name.split(' ');
        const lastAuthorName = fullLastAuthorNameList[0].replace(/[^a-z]|[.]/g, '');
        if (authorList.length === 2) {
          const firstAuthor = authorList[0];
          const fullFirstAuthorNameList = lastAuthor.name.split(' ');
          const firstAuthorName = fullFirstAuthorNameList[0].replace(/[^a-z]|[.]/g, '');

          if ((firstAuthor.contactId != null &&
              lastAuthorName.length < 2) ||
            (lastAuthor.contactId != null &&
              firstAuthorName.length < 2)) {
            fullName = false;
          }
        } else {
          if (lastAuthor.contactId == null &&
            lastAuthorName.length < 2) {
            fullName = false;
          }
        }
      }
    }

    return fullName ? null : {'fullNameEmail': {value: fullName}};
  };
}

