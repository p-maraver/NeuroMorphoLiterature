import {Component, Input, OnInit} from '@angular/core';
import {FullText} from './model/full-text';
import {ActivatedRoute} from '@angular/router';
import {ArticlesService} from '../details/services/articles.service';
import {DomSanitizer} from '@angular/platform-browser';
import {Classifier} from '../details/model/classifier';
import {Section} from './model/section';
import {FullTextService} from '../../services/full-text/full-text.service';


@Component({
  selector: 'app-full-text',
  templateUrl: './full-text.component.html',
  styleUrls: ['./full-text.component.css']
})


export class FullTextComponent implements OnInit {

  fullText: FullText;
  id: string;
  keyWordList: string[] = [];
  termList: string[] = [];
  classifier: Classifier;

  constructor(private route: ActivatedRoute,
              private fullTextService: FullTextService,
              private articlesService: ArticlesService,
              private sanitizer: DomSanitizer) {
  }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.getFullText();
  }

  getFullText(): void {
    this.fullTextService.findFullText(this.id)
      .subscribe(fullText => {
        this.fullText = fullText;
        this.getArticle();
      });
  }

  getArticle(): void {
    this.articlesService.findById(this.id)
      .subscribe(article => {
        if (article.classifier !== null) {
          this.keyWordList = Object.keys(article.classifier.keyWordList);
          this.termList = Object.keys(article.classifier.termList);

          this.classifier = article.classifier;
          // this.highLightSectionList(this.fullText.sectionList, []);
          this.highLight();
        }
      });
  }

  highLight(): void {
    this.fullText.title = this.highLightText(this.fullText.title);
    this.fullText.abstractContent = this.highLightText(this.fullText.abstractContent);
    this.fullText.figureList.forEach(figure => {
      figure.caption = this.highLightText(figure.caption);
    });
    this.highLightSectionList(this.fullText.sectionList);
  }

  highLightSectionList(sectionList: Section[]): void {
    if (sectionList) {
      sectionList.forEach(section => {
        this.highLightSection(section);
      });
    }
  }

  highLightSection(section: Section): void {
    if (section.paragraphList) {
      const newParagraphList = [];
      section.paragraphList.forEach(paragraph => {
        newParagraphList.push(this.highLightText(paragraph));
      });
      const newSection = section;
      newSection.paragraphList = newParagraphList;
    }
    if (section.sectionList != null) {
      this.highLightSectionList(section.sectionList);
    }
  }

  /*recursiveHighLightSectionList(sectionList): void {
    this.fullText.sectionList.forEach(section => {
      if (section.paragraphList) {
        section.paragraphList.forEach(paragraph => {
          this.result.sectionList = this.highLightText(paragraph);

        });
      }
    });
  }

  highLightSectionList(sectionList: Section[], result: Section[]): Section[] {
    // const newSectionList = sectionList.slice(1);
    const section = sectionList.pop();
    if (section !== null) {
      const newResul = this.highLightSection(section, result);
      result.concat(newResul);
      return result;
    } else {
      return this.highLightSectionList(newSectionList, result);
    }
  }

  */

  /*highLightSectionList(sectionList: Section[], result: Section[]): Section[] {
    if (sectionList) {
      sectionList.forEach(section => {
        const newSectionList = this.highLightSection(section, []);
        result.concat(newSectionList);
      });
    }
    console.log(result);

    return result;
  }

  highLightSection(section: Section, result: Section[]): Section[] {
    if (section.paragraphList) {
      const newParagraphList = [];
      section.paragraphList.forEach(paragraph => {
        newParagraphList.push(this.highLightText(paragraph));
      });
      const newSection = section;
      newSection.paragraphList = newParagraphList;
      // console.log(newSection);
      result.push(newSection);
    }
    if (section.sectionList != null) {
      return this.highLightSectionList(section.sectionList, result);
    } else {
      return result;
    }
  }
*/

  /*highLightSection(section: Section): Section {
    console.log(section.title);
    // section.title = this.highLightText(section.title);
    return section;
  }*/

  highLightText(text: string): string {

    const highlight = new Set();

    this.termList.forEach(term => {
      const wordList = this.classifier.termList[term];
      wordList.forEach(word => {
        highlight.add(word);
      });

    });
    this.keyWordList.forEach(keyword => {
      const wordList = this.classifier.keyWordList[keyword];
      wordList.forEach(word => {
        highlight.add(word);
      });
    });

    highlight.forEach(word => {
      word = ' ' + word + ' ';
      text = text.replace(new RegExp(word, 'g'), '<span class="keyWord" id=' + word + '>' + word + '</span>');
    });
    return this.sanitizer.bypassSecurityTrustHtml(text) as string;
  }


}
