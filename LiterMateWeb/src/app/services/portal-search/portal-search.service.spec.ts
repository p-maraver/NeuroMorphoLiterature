import { TestBed } from '@angular/core/testing';

import { PortalSearchService } from './portal-search.service';

describe('PortalSearchService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PortalSearchService = TestBed.get(PortalSearchService);
    expect(service).toBeTruthy();
  });
});
