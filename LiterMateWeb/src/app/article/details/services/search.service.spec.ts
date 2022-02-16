import { TestBed } from '@angular/core/testing';

import { CrossrefService } from './search.service';

describe('CrossrefService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CrossrefService = TestBed.get(CrossrefService);
    expect(service).toBeTruthy();
  });
});
