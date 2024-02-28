package com.musify.app




import android.util.Log
import androidx.lifecycle.ViewModel
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.PlaylistWithSongs
import com.musify.app.domain.models.Song
import com.musify.app.domain.repository.SongRepository
import com.musify.app.player.DownloadTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerController: PlayerController,
    private val songRepository: SongRepository,
    private val downloadTracker: DownloadTracker
) : ViewModel() {





    fun getPlayerController() = playerController


    fun getAllPlaylists(): Flow<List<Playlist>> {

        return songRepository.getAllPlaylists(Playlist.PLAYLIST)

    }



    fun addNewPlaylist(name:String){

        CoroutineScope(Dispatchers.IO).launch{
            songRepository.insertPlaylist(Playlist(name = name))

        }
    }


    fun addSongToPlaylist(song: Song, playlist: Playlist){

        CoroutineScope(Dispatchers.IO).launch{
            songRepository.insertSong(song)

            val crossRef = PlaylistSongCrossRef(playlist.playlistId, song.songId)

            songRepository.insertPlaylistSongCrossRef(crossRef)

        }
        if (playlist.downloadable){
            downloadTracker.download(song.toMediaItem())
        }
    }
}