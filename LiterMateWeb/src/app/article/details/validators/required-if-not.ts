import {AbstractControl, ValidatorFn} from '@angular/forms';
import {Collection} from '../model/collection';

export function requiredIfNot(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let valid = true;
    if (control !== undefined && control.value !== '') {
      if (control.get('articleStatus').value !== Collection.ToBeEvaluated
        && control.get('currentCollection').value !== Collection.Evaluated
      && (control.get('comment').value === null
        || control.get('comment').value === '')) {
        valid = false;
      }
    }
    return valid ? null : {'required': {value: valid}};
  };
}
