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

import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {ArticleComponent} from './article/article.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {FullTextComponent} from './article/full-text/full-text.component';
import {EmailComponent} from './article/email/email.component';
import {NewEmailComponent} from './article/email/new-email/new-email.component';
import {DetailsComponent} from './article/details/details.component';
import {SentEmailsComponent} from './article/email/sent-emails/sent-emails.component';
import {ArticleListComponent} from './article-list/article-list.component';
import {MetadataValuesComponent} from './metadata-values/metadata-values.component';
import {KeywordsSearchComponent} from './search/keywords-search/keywords-search.component';
import {PortalSearchComponent} from './search/portal-search/portal-search.component';
import {SearchComponent} from './search/search.component';
import {AgendaComponent} from './agenda/agenda.component';
import {ContactComponent} from './agenda/contact/contact.component';
import {TemplatesComponent} from './agenda/templates/templates.component';
import {ContactListComponent} from './agenda/contact-list/contact-list.component';
import {ConfigurationComponent} from './agenda/configuration/configuration.component';
import {ClassifierComponent} from './classifier/classifier.component';
import {TrainComponent} from './classifier/train/train.component';
import {KeywordsComponent} from './classifier/keywords/keywords.component';


const routes: Routes = [
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
  {path: 'article/:id/fulltext', component: FullTextComponent},
  {
    path: 'article/:id', component: ArticleComponent,
    children: [
      {path: '', redirectTo: 'details', pathMatch: 'full'},
      {path: 'details', component: DetailsComponent},
      /*{path: 'fulltext', component: FullTextComponent,
        children: [
          {path: '', redirectTo: 'new', pathMatch: 'full'},
          {path: 'section', component: SectionComponent},
          {path: 'images', component: FiguresComponent},
          {path: 'acknowledgment', component: AcknowledgmentComponent},
          {path: 'references', component: ReferencesComponent}
        ]
      },*/
      {
        path: 'emails', component: EmailComponent,
        children: [
          {path: '', redirectTo: 'new', pathMatch: 'full'},
          {path: 'new', component: NewEmailComponent},
          {path: 'sent', component: SentEmailsComponent},
        ]
      }
    ]
  },
  {path: 'articles/:collection', component: ArticleListComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'classifier', component: ClassifierComponent,
    children: [
      {path: '', redirectTo: 'train', pathMatch: 'full'},
      {path: 'train', component: TrainComponent},
      {path: 'keywords', component: KeywordsComponent},
    ]
  },
  {
    path: 'agenda', component: AgendaComponent,
    children: [
      {path: '', redirectTo: 'contacts', pathMatch: 'full'},
      {path: 'configuration', component: ConfigurationComponent},
      {path: 'contacts', component: ContactListComponent},
      {path: 'contacts/:id', component: ContactComponent},
      {path: 'templates', component: TemplatesComponent}
    ]
  },
  {path: 'metadata', component: MetadataValuesComponent},
  {
    path: 'search', component: SearchComponent,
    children: [
      {path: 'portals', component: PortalSearchComponent},
      {path: 'keywords', component: KeywordsSearchComponent}
    ]
  }
];


@NgModule({
  imports: [RouterModule.forRoot(routes, {paramsInheritanceStrategy: 'always'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
