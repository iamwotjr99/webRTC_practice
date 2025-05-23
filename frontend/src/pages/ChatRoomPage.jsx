import BottomBar from "../components/roomin/BottomBar";
import VideoGrid from "../components/roomin/VideoGrid";

export default function ChatRoomPage() {

    const users = [
        { id: 1, nickname: "재석군" },
        { id: 2, nickname: "재석군" },
        { id: 3, nickname: "재석군" },
        { id: 4, nickname: "재석군" },
        { id: 5, nickname: "재석군" },
        { id: 6, nickname: "재석군" },
    ];

    return (
        <>
            <VideoGrid users={users}/>
            <BottomBar />
        </>
    )
}