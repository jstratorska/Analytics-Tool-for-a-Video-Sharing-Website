import {IPlaylist} from './playlist';
import {IVideo} from './video';

export interface ISelection {
    playlists: IPlaylist[],
    videos: IVideo[],
    statistic: string,
    color: string
    selectedVideoId: string
}