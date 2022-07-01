import { TestBed } from '@angular/core/testing';

import { MapUtilService } from './map-util.service';

describe('MapUtilService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MapUtilService = TestBed.get(MapUtilService);
    expect(service).toBeTruthy();
  });
});
