import Morphism from 'morphism';

export interface ICalendars {
  calendarId: string;
  calendarName: string;
}
export const mapResponseToCalendars = Morphism({
  calendarId: (source: any) => source.calendarId,
  calendarName: (source: any) => source.calendarName
}as Record<keyof ICalendars, any>);
