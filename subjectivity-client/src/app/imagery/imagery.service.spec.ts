import { TestBed } from '@angular/core/testing';

import { ImageryService } from './imagery.service';

describe('ImageryService', () => {
  let service: ImageryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
