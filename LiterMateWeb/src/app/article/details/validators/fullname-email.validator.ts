import {AbstractControl, ValidatorFn} from '@angular/forms';
import {Collection} from '../model/collection';

export function fullNameEmail(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let fullName = true;
    const authorList = control.get('data.authorList').value;
    if (control.get('status.articleStatus').value === Collection.Positive) {
      if (control !== undefined && authorList != null && authorList.length > 0) {
        const lastAuthor = authorList[authorList.length - 1];
        const fullLastAuthorNameList = lastAuthor.name.split(' ');
        const lastAuthorName = fullLastAuthorNameList[0].replace(/[^a-z]|[.]/g, '');
        if (authorList.length === 2) {
          const firstAuthor = authorList[0];
          const fullFirstAuthorNameList = lastAuthor.name.split(' ');
          const firstAuthorName = fullFirstAuthorNameList[0].replace(/[^a-z]|[.]/g, '');

          if ((firstAuthor.contactId != null &&
              lastAuthorName.length < 2) ||
            (lastAuthor.contactId != null &&
              firstAuthorName.length < 2)) {
            fullName = false;
          }
        } else {
          if (lastAuthor.contactId == null &&
            lastAuthorName.length < 2) {
            fullName = false;
          }
        }
      }
    }

    return fullName ? null : {'fullNameEmail': {value: fullName}};
  };
}

