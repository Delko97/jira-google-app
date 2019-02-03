import {Component, OnInit} from '@angular/core';
import {ILanguages} from '../../entities/ILanguages';
import {TranslateService} from '@ngx-translate/core';
import {Languages} from '../../entities/language.constants';

@Component({
  selector: 'app-language',
  templateUrl: './language.component.html',
  styleUrls: ['./language.component.scss']
})
export class LanguageComponent implements OnInit {
  public currentLang = Languages[this.translateService.currentLang];

  languages: ILanguages[] = [
    {value: 'en', viewValue: 'English'},
    {value: 'sk', viewValue: 'Slovak'},
  ];

  constructor(private translateService: TranslateService) {
  }

  ngOnInit() {
  }

  changeLanguage(event) {
   this.translateService.use(event.target.value);
  }
}
