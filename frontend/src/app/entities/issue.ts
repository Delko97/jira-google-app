import Morphism from 'morphism';
import {mapStringToDate} from '../helpers/helper';

export interface Issue {
  issuename: string;
  asignee: string;
  asignee_email: string;
  projectName: string;
  customfield_10033: string;
  customfield_10034: string;
  customfield_10014: string;
  created_date: string;
  description: string;
  summary: string;
  creator_name: string;
  creator_email: string;
  reporter_name: string;
  reporter_email: string;
  duedate: Date;
  includes: any;
}

export const mapResponseToIssue = Morphism({
  issuename: (source: any) => source.issuetype.name,
  asignee: (source: any) => source.assignee.name,
  asignee_email: (source: any) => source.assignee.emailAddress,
  projectName: (source: any) => source.project.name,
  customfield_10033: (source: any) => source.customfield_10033,
  customfield_10034: (source: any) => source.customfield_10034,
  customfield_10014: (source: any) => source.customfield_10014,
  created_date: (source: any) => mapStringToDate(source.created),
  description: (source: any) => source.description,
  summary: (source: any) => source.summary,
  creator_name: (source: any) => source.creator.name,
  creator_email: (source: any) => source.creator.emailAddress,
  reporter_name: (source: any) => source.reporter.name,
  reporter_email: (source: any) => source.reporter.emailAddress,
  duedate: (source: any) => mapStringToDate(source.duedate),
} as Record<keyof Issue, any>);
