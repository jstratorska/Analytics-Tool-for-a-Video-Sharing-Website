import {IPlaylist} from '../playlist';
import {IVideo} from '../video';
import {ICategory} from '../category';

export interface IGraphSelector {
    categories: ICategory[],
    playlists: IPlaylist[],
    videos: IVideo[],
    statistic: string,
    color: string
    selectedVideoId: string
}