/*
 * Copyright (c) 2015-2022, Patricia Maraver
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import {Component, OnInit} from '@angular/core';
import {ReleaseService, TaskProperties, TaskStatus} from '../services/release/release.service';
import {ReleaseVersion} from '../services/release/release-version';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {concatMap, map} from 'rxjs/operators';
import {of, throwError} from 'rxjs';
import {MatSnackBar} from '@angular/material';


interface RollbackVersionOption {
  value: string | null;
  viewValue: string;
}

@Component({
  selector: 'app-release',
  templateUrl: './release.component.html',
  styleUrls: ['./release.component.css'],
})
export class ReleaseComponent implements OnInit {

  constructor(
    private releaseService: ReleaseService,
    private fb1: FormBuilder,
    private fb2: FormBuilder,
    private snackBar: MatSnackBar
  ) {
  }


  versionForm: FormGroup;
  rollbackForm: FormGroup;
  oldRelease: ReleaseVersion;
  protected readonly TaskStatus = TaskStatus;

  tasksStatus_ReleaseInReview: TaskStatus[] = [null, null, null, null];
  executing_ReleaseInReview = false;

  tasksStatus_GenerateReports: TaskStatus[] = [null, null, null, null];
  executing_GenerateReports = false;
  successfully_GenerateReports = false;

  tasksStatus_ReleaseInMain: TaskStatus[] = [null];
  executing_ReleaseInMain = false;

  tasksStatus_LaunchBibliometric: TaskStatus[] = [null];
  executing_LaunchBibliometric = false;

  tasksStatus_Rollback: TaskStatus[] = [null];
  executing_Rollback = false;
  optionsRollbackVersion: RollbackVersionOption[];
  isLoading_RollbackVersion = false;
  selectedOption: RollbackVersionOption;
  optionsErrorMessage: string | null = null;


  ngOnInit() {
    this.versionForm = this.fb1.group({
      version_string: ['', Validators.required],
      version_date: [new Date().toISOString(), [Validators.required]]
    });
    this.rollbackForm = this.fb2.group({
      selectedOption: ['', Validators.required]
    });

    // Load last version into version form
    this.releaseService.findVersion()
      .subscribe(
        data => {
          this.oldRelease = Object.assign({}, data, {date: new Date(data.date)});
          this.versionForm.patchValue({
            version_string: this.oldRelease.version,
            version_date: this.oldRelease.date
          });
        },
        _err => {
          this.snackBar.open('Error getting version data from server', 'Error');
        }
      );

    // Load options into selector to rollback
    this.isLoading_RollbackVersion = true;
    this.releaseService.getVersionList()
      .pipe(
        map(versionList =>
          versionList.map(version => ({
            value: version,
            viewValue: version
          }))
        )
      ).subscribe(
      options => {
        this.optionsRollbackVersion = options;
        this.isLoading_RollbackVersion = false;
        this.optionsErrorMessage = null;
      },
      _err => {
        this.optionsErrorMessage = 'Error fetching versions';
        this.isLoading_RollbackVersion = false;
      }
    );

  }

  getVersionIdErrorMessage() {
    return this.versionForm.get('version_string').hasError('required') ? 'You must enter a value' : '';
  }


  startReleaseReview() {

    if (this.versionForm.invalid) {
      this.snackBar.open('Invalid form data', 'Done');
      return;
    }

    const releaseVersionBody = {
      id: this.oldRelease.id,
      date: this.versionForm.get('version_date').value,
      type: this.oldRelease.type,
      version: this.versionForm.get('version_string').value
    };

    this.tasksStatus_ReleaseInReview[0] = TaskStatus.processing;
    this.executing_ReleaseInReview = true;
    this.releaseService.updateVersion(releaseVersionBody)
      .pipe(
        concatMap(taskResult => {
          if (taskResult.status === TaskStatus.success) {
            this.tasksStatus_ReleaseInReview[0] = TaskStatus.success;
            this.tasksStatus_ReleaseInReview[1] = TaskStatus.processing;
            return this.releaseService.release('dump', releaseVersionBody.version);
          }
          if (taskResult.status === TaskStatus.error) {
            this.tasksStatus_ReleaseInReview[0] = TaskStatus.error;
            return throwError(taskResult);
          }
        }),
        concatMap(taskResult => {
          if (taskResult.status === TaskStatus.success) {
            this.tasksStatus_ReleaseInReview[1] = TaskStatus.success;
            this.tasksStatus_ReleaseInReview[2] = TaskStatus.processing;
            return this.releaseService.release('scp2remote', releaseVersionBody.version);
          }
          if (taskResult.status === TaskStatus.error) {
            this.tasksStatus_ReleaseInReview[1] = TaskStatus.error;
            return throwError(taskResult);
          }
        }),
        concatMap(taskResult => {
          if (taskResult.status === TaskStatus.success) {
            this.tasksStatus_ReleaseInReview[2] = TaskStatus.success;
            this.tasksStatus_ReleaseInReview[3] = TaskStatus.processing;
            return this.releaseService.release('restore', releaseVersionBody.version, 'review');
          }
          if (taskResult.status === TaskStatus.error) {
            this.tasksStatus_ReleaseInReview[2] = TaskStatus.error;
            return throwError(taskResult);
          }
        }),
        concatMap(taskResult => {
          console.log(taskResult);
          if (taskResult.status === TaskStatus.success) {
            this.tasksStatus_ReleaseInReview[3] = TaskStatus.success;
            return of(taskResult);
          }
          if (taskResult.status === TaskStatus.error) {
            this.tasksStatus_ReleaseInReview[3] = TaskStatus.error;
            return throwError(taskResult);
          }
        })
      )
      .subscribe(
        next => {
          console.log(next);
          this.executing_ReleaseInReview = false;
          this.snackBar.open('All tasks completed', 'Close');
        },
        (error: TaskProperties) => {
          console.log(error);
          this.executing_ReleaseInReview = false;
          this.snackBar.open(error.message, 'Error');

        }
      );

  }

  startGenerateReports() {

    this.tasksStatus_GenerateReports[0] = TaskStatus.processing;
    this.executing_GenerateReports = true;
    this.successfully_GenerateReports = false;
    this.releaseService.generateReport('CompleteDetails')
      .pipe(
        concatMap(taskResult => {
          if (taskResult.status === TaskStatus.success) {
            this.tasksStatus_GenerateReports[0] = TaskStatus.success;
            this.tasksStatus_GenerateReports[1] = TaskStatus.processing;
            return this.releaseService.generateReport('FrozenEvolution');
          }
          if (taskResult.status === TaskStatus.error) {
            this.tasksStatus_GenerateReports[0] = TaskStatus.error;
            return throwError(taskResult);
          }
        }),
        concatMap(taskResult => {
          if (taskResult.status === TaskStatus.success) {
            this.tasksStatus_GenerateReports[1] = TaskStatus.success;
            this.tasksStatus_GenerateReports[2] = TaskStatus.processing;
            return this.releaseService.release('scpFromRemote');
          }
          if (taskResult.status === TaskStatus.error) {
            this.tasksStatus_GenerateReports[1] = TaskStatus.error;
            return throwError(taskResult);
          }
        }),
        concatMap(taskResult => {
          if (taskResult.status === TaskStatus.success) {
            this.tasksStatus_GenerateReports[2] = TaskStatus.success;
            return of(taskResult);
          }
          if (taskResult.status === TaskStatus.error) {
            this.tasksStatus_GenerateReports[2] = TaskStatus.error;
            return throwError(taskResult);
          }
        })
      )
      .subscribe(
        next => {
          console.log(next);
          this.executing_GenerateReports = false;
          this.successfully_GenerateReports = true;
          this.snackBar.open('All tasks completed', 'Close');
        },
        (error: TaskProperties) => {
          console.log(error);
          this.executing_GenerateReports = false;
          this.snackBar.open(error.message, 'Error');

        }
      );

  }

  startReleaseMain() {

    if (this.versionForm.invalid) {
      this.snackBar.open('Invalid form data', 'Done');
      return;
    }

    const releaseVersionBody = {
      id: this.oldRelease.id,
      date: this.versionForm.get('version_date').value,
      type: this.oldRelease.type,
      version: this.versionForm.get('version_string').value
    };

    this.tasksStatus_ReleaseInMain[0] = TaskStatus.processing;
    this.executing_ReleaseInMain = true;
    this.releaseService.release('restore', releaseVersionBody.version, 'main')
      .pipe(
        concatMap(taskResult => {
          if (taskResult.status === TaskStatus.success) {
            this.tasksStatus_ReleaseInMain[0] = TaskStatus.success;
            return of(taskResult);
          }
          if (taskResult.status === TaskStatus.error) {
            this.tasksStatus_ReleaseInMain[0] = TaskStatus.error;
            return throwError(taskResult);
          }
        })
      )
      .subscribe(
        next => {
          console.log(next);
          this.executing_ReleaseInMain = false;
          this.snackBar.open('All tasks completed', 'Close');
        },
        (error: TaskProperties) => {
          console.log(error);
          this.executing_ReleaseInMain = false;
          this.snackBar.open(error.message, 'Error');

        }
      );

  }

  webHook() {

    this.tasksStatus_LaunchBibliometric[0] = TaskStatus.processing;
    this.executing_LaunchBibliometric = true;
    this.releaseService.startWebHook()
      .pipe(
        concatMap(taskResult => {
          if (taskResult.status === TaskStatus.success) {
            this.tasksStatus_LaunchBibliometric[0] = TaskStatus.success;
            return of(taskResult);
          }
          if (taskResult.status === TaskStatus.error) {
            this.tasksStatus_LaunchBibliometric[0] = TaskStatus.error;
            return throwError(taskResult);
          }
          // Special case where server non-responsiveness suggests
          // non-compliance with protocol standards.
          // The response will be handled as a success.
          if (taskResult.status === TaskStatus.special) {
            this.tasksStatus_LaunchBibliometric[0] = TaskStatus.special;
            return of(taskResult);
          }
        })
      )
      .subscribe(
        next => {
          this.executing_LaunchBibliometric = false;
          this.snackBar.open('All tasks completed', 'Close');
        },
        (error: TaskProperties) => {
          this.executing_LaunchBibliometric = false;
          this.snackBar.open(error.message, 'Error');

        }
      );

  }

  onRollbackSubmit() {

    if (this.rollbackForm.invalid || this.selectedOption.value === null) {
      this.snackBar.open('Invalid data selected to rollback', 'Done');
      return;
    }

    this.tasksStatus_Rollback[0] = TaskStatus.processing;
    this.executing_Rollback = true;
    this.releaseService.release('restore', this.selectedOption.value, 'review')
      .pipe(
        concatMap(taskResult => {
          if (taskResult.status === TaskStatus.success) {
            this.tasksStatus_Rollback[0] = TaskStatus.success;
            return of(taskResult);
          }
          if (taskResult.status === TaskStatus.error) {
            this.tasksStatus_Rollback[0] = TaskStatus.error;
            return throwError(taskResult);
          }
        })
      )
      .subscribe(
        next => {
          console.log(next);
          this.executing_Rollback = false;
          this.snackBar.open('All tasks completed', 'Close');
        },
        (error: TaskProperties) => {
          console.log(error);
          this.executing_Rollback = false;
          this.snackBar.open(error.message, 'Error');

        }
      );

  }


}
