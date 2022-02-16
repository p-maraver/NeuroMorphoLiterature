import {AbstractControl, ValidatorFn} from '@angular/forms';
import {Collection} from '../model/collection';

export function existEmail(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let existMail = true;
    if (control !== undefined) {
      if (
        control.get('status.articleStatus').value === Collection.Positive &&
        control.get('status.currentCollection').value !== Collection.NeuroMorpho &&
        control.get('status.currentCollection').value !== Collection.NeuroMorphoEvaluated) {
        existMail = false;
        control.get('data.authorList').value.forEach(author => {
          if (author.contactId !== null) {
            existMail = true;
          }
        });
      }
    }
    return existMail ? null : {'existEmail': {value: existMail}};
  };
}

