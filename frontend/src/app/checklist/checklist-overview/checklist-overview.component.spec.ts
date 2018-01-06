import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChecklistOverviewComponent } from './checklist-overview.component';

describe('ChecklistOverviewComponent', () => {
  let component: ChecklistOverviewComponent;
  let fixture: ComponentFixture<ChecklistOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChecklistOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChecklistOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
