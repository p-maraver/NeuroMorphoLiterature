import {AbstractControl, ValidatorFn} from '@angular/forms';

export function atLeastOne(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {

    let valid = true;

    const usage = Object.values(control.get('usage').value);
    if (usage.every(function (k) { return k === false; })) {
          valid = false;
    }
    return valid ? null : {'required': {value: valid}};
  };
}



