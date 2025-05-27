import { useNavigate } from "react-router";
import Button from "../common/Button";

export default function RoomCard({ room }) {
    const navigate = useNavigate();

    return (
        <article className="bg-white p-4 rounded shadow flex items-center justify-between">
            <div>
                <h3 className="font-bold">{room.title}</h3>
                <p className="text-sm text-gray-500">
                    {room.participant} / {room.capacity}명 참여중
                </p>
            </div>
            <Button
                onClick={() => navigate(`/chatroom/${room.roomId}`)}
                className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 font-semibold transition">
                    참여하기
            </Button>
        </article>
    )
}