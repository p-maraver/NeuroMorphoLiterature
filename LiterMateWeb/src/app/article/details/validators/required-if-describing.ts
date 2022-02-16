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



