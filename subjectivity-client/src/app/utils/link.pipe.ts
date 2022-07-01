import { Pipe, PipeTransform } from '@angular/core';
import {environment} from "../../environments/environment";

const serverUrl = environment.baseURL + '/' + environment.context + '/';

@Pipe({
  name: 'link'
})
export class LinkPipe implements PipeTransform {
  transform(value: any, type: String): any {
    if (value && type) {
      if (type == 'image') {
        return serverUrl + 'similars/' + value;
      }
    }
    return;
  }
}
