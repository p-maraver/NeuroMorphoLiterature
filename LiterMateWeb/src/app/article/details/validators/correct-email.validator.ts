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

export function validEmail(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let validMail = true;
    // const EMAIL_REGEXP = /^\S+@\S+\.\S+$/;
    const EMAIL_REGEXP = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,4}$/;

    if (control !== undefined) {
      validMail = true;
      control.get('data.authorList').value.forEach(author => {
        if (author.emailList !== null && author.emailList.length !== 0) {
          author.emailList.forEach(email => {
              if (!EMAIL_REGEXP.test(email)) {
                validMail = false;
              }
            }
          );
        }
      });

    }
    return validMail ? null : {'validMail': {value: validMail}};
  };
}

