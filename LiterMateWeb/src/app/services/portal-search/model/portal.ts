

export class Portal {
  id: string;
  name: string;
  url: string;
  base: string;
  startSearchDate: Date;
  endSearchDate: Date;
  active: boolean;
  searchUrlApi: string;
  contentUrlApi: string;
  token: string;
  log: Log;

}

class Log {
  start: Date;
  stop: Date;
  cause: string;
}

