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

export function requiredIfDescribing(isNewArticle: boolean): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let valid = true;
    if (control !== undefined) {

      if (!isNewArticle && control.get('usage').value['describing']) {
        const keys = Object.keys(control.get('metadata').value);
        keys.forEach(key => {
          if (key === 'tracingSystem' || key === 'cellType' || key === 'nReconstructions') {
            const value = control.value['metadata'][key];
            if (value === null || value === undefined || value === '' || value.length === 0) {
              valid = false;
            }
          }
        });
      }
      return valid ? null : {'required': {value: valid}};
    }
  };
}



