

export class Contact {
  id: string;
  firstName: string;
  lastName: string;
  emailList:  ContactEmail[];
  unsubscribed: boolean;
  replacedListId: string[];

  constructor(unsubscribed: boolean) {
    this.unsubscribed = unsubscribed;
  }
}

export class ContactEmail {
  email: string;
  bounced: boolean;
}
