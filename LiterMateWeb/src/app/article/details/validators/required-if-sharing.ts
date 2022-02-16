import {AbstractControl, ValidatorFn} from '@angular/forms';

export function requiredIfSharing(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let valid = true;
    if (control.get('usage').value['sharing']) {
      if (control.get('shared').value !== null && control.get('shared.sharedList').value.length < 1) {
        valid = false;
      }
    }
    return valid ? null : {'required': {value: valid}};
  };
}



