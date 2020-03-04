import React from 'react'
import {Player} from 'video-react'
import './VideoPlayer.css'
import classNames from 'classnames'

export default class VideoPlayer extends React.Component {
    render() {
        return (
            <div className={'video-player-backdrop'}>
                <div className={classNames('video-player-wrapper', 'enter-from-top')}>
                    <Player
                        playsInline
                        autoPlay={true}
                        poster='/assets/poster.png'
                        src='https://media.w3.org/2010/05/sintel/trailer_hd.mp4'
                    />
                </div>
            </div>
        )
    }
}
