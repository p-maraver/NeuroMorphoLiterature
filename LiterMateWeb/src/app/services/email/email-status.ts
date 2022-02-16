export enum EmailStatus {
  invitation = 'To be requested',
  reminder = 'Invited',
  pestering = 'Reminder',
  ultimatum = 'Pestering',
  waitingResponse = 'Ultimatum',
  positiveResponse = 'Positive response',
  bounced = 'Bounced',
  bouncedNegative = 'Bounced Negative',

}
