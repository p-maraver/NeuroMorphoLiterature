import {AbstractControl, ValidatorFn} from '@angular/forms';
import {Collection} from '../../article/details/model/collection';

export function validEmail(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let validMail = true;
    // tslint:disable-next-line:max-line-length
    const EMAIL_REGEXP = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,4}$/;
    validMail = true;
    if (control !== undefined && control.value !== '') {
      control.value.forEach(value => {
        if (!EMAIL_REGEXP.test(value.email)) {
          validMail = false;
        }
      });
    }
    return validMail ? null : {'validMail': {value: validMail}};
  };
}


export function validEmailContact(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {

    let validMail = true;
    // tslint:disable-next-line:max-line-length
    const EMAIL_REGEXP = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,4}$/;
    if (control !== undefined && control.value !== null && control.value.emailList.length > 0) {
      control.value.emailList.forEach(value => {
        if (!EMAIL_REGEXP.test(value.email)) {
          validMail = false;
        }
      });
    }
    return validMail ? null : {'validMail': {value: validMail}};
  };
}

export function existEmailContact(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let existMail = false;
    if (control !== undefined) {
      if (control.value !== null && control.value.emailList.length > 0) {
        existMail = true;
      }
    }
    return existMail ? null : {'existMail': {value: existMail}};
  };
}

