package com.example.fitnesstracker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

public class SpotifyUtils {
    public static void openSpotifyPlaylist(Context context, String playlistUrl) {
        try {
            // Check if Spotify is installed
            context.getPackageManager().getPackageInfo("com.spotify.music", 0);
            
            // Open the playlist in Spotify app
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(playlistUrl));
            intent.putExtra(Intent.EXTRA_REFERRER, 
                Uri.parse("android-app://" + context.getPackageName()));
            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // Spotify not installed, open in browser instead
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(playlistUrl));
            context.startActivity(intent);
            
            Toast.makeText(context, 
                "Spotify app not found. Opening in browser instead.", 
                Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, 
                "Could not open Spotify playlist: " + e.getMessage(), 
                Toast.LENGTH_SHORT).show();
        }
    }
} 