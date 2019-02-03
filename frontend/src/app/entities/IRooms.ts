import Morphism from 'morphism';
import {mapStringToDate} from '../helpers/helper';

export interface IRooms {
  eventId: string;
  title: string;
  description: string;
  location: string;
  startDate: Date;
  endDate: Date;
}

export const mapResponseToRooms = Morphism({
  eventId: (source: any) => source.id,
  title: (source: any) => source.summary,
  description: (source: any) => source.description,
  location: (source: any) => source.location,
  startDate: (source: any) => mapStringToDate(source.start.dateTime.value),
  endDate: (source: any) => mapStringToDate(source.end.dateTime.value),
}as Record<keyof IRooms, any>);
