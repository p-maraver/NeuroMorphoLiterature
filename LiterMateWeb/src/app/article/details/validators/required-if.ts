import {AbstractControl, ValidatorFn} from '@angular/forms';

export function requiredIf(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let valid = false;

    if (control !== undefined) {
      valid = control.value.data.notDoi ||
        (control.value.data.doi !== null && control.value.data.doi !== '');
    }
    return valid ? null : {'required': {value: valid}};
  };
}



