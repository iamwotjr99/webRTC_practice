import { Link } from "react-router";
import Button from "./common/Button";

export default function RoomCard({ room }) {
    return (
        <article className="bg-white p-4 rounded shadow flex items-center justify-between">
            <div>
                <h3 className="font-bold">{room.title}</h3>
                <p className="text-sm text-gray-500">
                    {room.participants} / {room.capacity}명 참여중
                </p>
            </div>
            <Link to={`chatroom/${room.id}`}
                className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 font-semibold transition">
                    참여하기
            </Link>
        </article>
    )
}