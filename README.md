<div align="center">
<img src="fastlane/metadata/android/en-US/images/icon.png" width="160" height="160" style="display: block; margin: 0 auto"/>
<h1>musicc</h1>
<p>A music client that fuses Spotify and YouTube Music into one seamless experience</p>

[![Latest release](https://img.shields.io/github/v/release/Nigam57/musicc?style=for-the-badge)](https://github.com/Nigam57/musicc/releases/latest)
[![GitHub license](https://img.shields.io/github/license/Nigam57/musicc?style=for-the-badge)](https://github.com/Nigam57/musicc/blob/main/LICENSE)
[![Downloads](https://img.shields.io/github/downloads/Nigam57/musicc/total?style=for-the-badge)](https://github.com/Nigam57/musicc/releases)

</div>

## What is musicc?

**musicc** is an Android music client that brings together the best of Spotify and YouTube Music. It uses your Spotify account to power personalized recommendations, search, and home content — while streaming audio through YouTube Music.

With aggressive precaching built-in, musicc delivers instant playback and a buttery smooth listening experience.

### Why musicc?

- **Spotify's personalization** — Your top tracks, favorite artists, and curated playlists from Spotify drive the recommendations
- **YouTube Music's catalog** — Access YouTube Music's vast library for streaming, including rare tracks, live performances, and remixes
- **Aggressive Caching** — Pre-loads tracks so playback starts instantly
- **No setup required** — Just log in with your Spotify account directly in the app. No developer dashboard, no Client ID, no extra steps
- **No Spotify Premium required** — musicc uses Spotify's data APIs (not streaming), so a free Spotify account is all you need
- **Built-in recommendation engine** — A custom algorithm builds personalized queues using your Spotify listening history

## Features

### Spotify Integration
- **Spotify as search source** — Search results powered by Spotify, with automatic YouTube Music matching for playback
- **Spotify as home source** — Home screen populated with your Spotify top tracks, top artists, playlists, and new releases
- **Spotify-only mode** — Option to hide all YouTube-based content and show exclusively Spotify-powered sections on the home screen
- **Smart queue generation** — Custom recommendation engine that builds radio-like queues from your Spotify taste profile
- **Spotify library sync** — Access your Spotify playlists and liked songs directly in the app
- **Spotify-to-YouTube matching** — Fuzzy matching algorithm with local caching for fast, accurate track resolution
- **Manual match override** — If a Spotify track is matched to the wrong YouTube video, you can manually fix it by pasting the correct YouTube link
- **Spotify album browsing** — Dedicated album screen for Spotify albums with full tracklist, metadata, and one-tap playback

### Lossless Audio (Experimental)
- **Qobuz backend** — Optional FLAC and Hi-Res streaming via the Qobuz catalog, replacing YouTube Music's lossy audio
- **Deterministic matching** — Uses ISRC so Spotify-sourced tracks resolve to their exact Qobuz counterpart without ambiguity
- **Multi-backend fallback** — Independent Qobuz resolvers are tried in sequence if the primary one is rate-limited
- **Automatic YouTube fallback** — If a track isn't on Qobuz, playback falls back silently to the standard YouTube Music stream

### Core Music Features
- Play any song or video from YouTube Music
- Background playback and lyrics
- Offline listening support
- Discord Rich Presence integration

## Credits & Upstream
**musicc** is proudly built on the incredible open-source foundation of **Meld** (by FrancescoGrazioso), which in turn was built upon **Metrolist** (by Mo Agamy). We extend our massive gratitude to all the upstream developers who made this project possible.

## License
This project is licensed under the GPL-3.0 License.
