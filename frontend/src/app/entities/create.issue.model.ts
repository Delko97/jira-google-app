import {Field} from './field.model';

export class Wrapper {

  constructor(
    public key?: string,
    public field?: Field,
  ) {
  }
}
