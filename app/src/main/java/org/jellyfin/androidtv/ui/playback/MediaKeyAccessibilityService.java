package org.jellyfin.androidtv.ui.playback;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

/**
 * Receives media keys (play/pause, fast-forward, rewind) that some TV boxes
 * intercept before they can reach the player window. The keys are forwarded
 * to the playback overlay only while a video is playing; at all other times
 * they are passed through untouched.
 */
public class MediaKeyAccessibilityService extends AccessibilityService {
    private static CustomPlaybackOverlayFragment activeFragment;

    public static void setActiveFragment(CustomPlaybackOverlayFragment fragment) {
        activeFragment = fragment;
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_DOWN) return false;

        int keyCode = event.getKeyCode();
        if (keyCode != KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                && keyCode != KeyEvent.KEYCODE_MEDIA_PLAY
                && keyCode != KeyEvent.KEYCODE_MEDIA_PAUSE
                && keyCode != KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
                && keyCode != KeyEvent.KEYCODE_MEDIA_REWIND) {
            return false;
        }

        CustomPlaybackOverlayFragment fragment = activeFragment;
        if (fragment == null) return false;

        return fragment.handleInterceptedMediaKey(keyCode);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Not used; the service only filters key events.
    }

    @Override
    public void onInterrupt() {
        // Not used.
    }
}
