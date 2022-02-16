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

