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

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatAutocompleteModule,
  MatBadgeModule,
  MatButtonModule,
  MatDatepickerModule,
  MatCardModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDialogModule,
  MatDividerModule,
  MatExpansionModule,
  MatFormFieldModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatSelectModule,
  MatSidenavModule,
  MatSlideToggleModule,
  MatSnackBarModule,
  MatSortModule,
  MatStepperModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule} from '@angular/material';

import { AppComponent } from './app.component';
import { ArticleComponent } from './article/article.component';
import { AppRoutingModule } from './app-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';


import {AngularEditorModule} from '@kolkov/angular-editor';
import {FullTextComponent} from './article/full-text/full-text.component';
import {SectionComponent} from './article/full-text/section/section.component';
import {FiguresComponent} from './article/full-text/figures/figures.component';
import {ReferencesComponent} from './article/full-text/references/references.component';
import {AcknowledgmentComponent} from './article/full-text/acknowledgment/acknowledgment.component';
import {AbstractComponent} from './article/full-text/abstract/abstract.component';
import {EmailComponent} from './article/email/email.component';
import { NewEmailComponent } from './article/email/new-email/new-email.component';
import { DetailsComponent } from './article/details/details.component';
import {ReconstructionsComponent} from './article/details/reconstructions/reconstructions.component';
import {MetadataComponent} from './article/details/metadata/metadata.component';
import {DataComponent} from './article/details/data/data.component';
import {SharedDialogComponent} from './article/details/metadata/shared-dialog.component';
import { SentEmailsComponent } from './article/email/sent-emails/sent-emails.component';
import { ReleaseComponent } from './release/release.component';
import {SupplementaryMaterialComponent} from './article/full-text/supplementary-material/supplementary-material.component';
import { MergeDialogComponent } from './article/details/data/merge-dialog.component';
import { ArticleListComponent } from './article-list/article-list.component';
import { MetadataValuesComponent } from './metadata-values/metadata-values.component';
import { MetadataDialogComponent } from './metadata-values/metadata-dialog.component';
import { RowColorDirective } from './article-list/row-color.directive';
import {PortalSearchComponent} from './search/portal-search/portal-search.component';
import {KeywordsSearchComponent} from './search/keywords-search/keywords-search.component';
import { SearchComponent } from './search/search.component';
import {KeywordDialogComponent} from './search/keywords-search/keyword-dialog.component';
import {PortalDialogComponent} from './search/portal-search/portal-dialog.component';
import { AgendaComponent } from './agenda/agenda.component';
import {ContactComponent} from './agenda/contact/contact.component';
import { AuthorDialogComponent } from './article/details/data/author-dialog/author-dialog.component';
import {TemplatesComponent} from './agenda/templates/templates.component';
import { ContactListComponent } from './agenda/contact-list/contact-list.component';
import { ConfigurationComponent } from './agenda/configuration/configuration.component';
import { StatisticsComponent } from './article-list/statistics/statistics.component';
import { BulkStatusComponent } from './article-list/bulk-status/bulk-status.component';
import {ClassifierComponent} from './classifier/classifier.component';
import {ChartsModule, ThemeService} from 'ng2-charts';
import { KeywordsComponent } from './classifier/keywords/keywords.component';
import { TrainComponent } from './classifier/train/train.component';
import { Dashboard2Component } from './dashboard2/dashboard2.component';

@NgModule({
  declarations: [
    AppComponent,
    ArticleComponent,
    DashboardComponent,
    ReconstructionsComponent,
    MetadataComponent,
    DataComponent,
    SharedDialogComponent,
    FullTextComponent,
    SectionComponent,
    FiguresComponent,
    ReferencesComponent,
    AcknowledgmentComponent,
    AbstractComponent,
    EmailComponent,
    TemplatesComponent,
    NewEmailComponent,
    DetailsComponent,
    SentEmailsComponent,
    ReleaseComponent,
    ContactComponent,
    SupplementaryMaterialComponent,
    MergeDialogComponent,
    ArticleListComponent,
    MetadataValuesComponent,
    MetadataDialogComponent,
    RowColorDirective,
    PortalSearchComponent,
    KeywordsSearchComponent,
    SearchComponent,
    KeywordDialogComponent,
    PortalDialogComponent,
    AgendaComponent,
    AuthorDialogComponent,
    ContactListComponent,
    ConfigurationComponent,
    StatisticsComponent,
    BulkStatusComponent,
    ClassifierComponent,
    KeywordsComponent,
    TrainComponent,
    Dashboard2Component
  ],
  imports: [
    AngularEditorModule,
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    FormsModule,
    HttpClientModule,
    MatAutocompleteModule,
    MatBadgeModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatSelectModule,
    MatSidenavModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatStepperModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    ReactiveFormsModule,
    ChartsModule
  ],
  providers: [ThemeService],
  bootstrap: [AppComponent],
  entryComponents: [
    SharedDialogComponent,
    MergeDialogComponent,
    MetadataDialogComponent,
    KeywordDialogComponent,
    PortalDialogComponent,
    AuthorDialogComponent]
})
export class AppModule { }
