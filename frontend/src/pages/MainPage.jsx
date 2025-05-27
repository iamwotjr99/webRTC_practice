import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router";
import { getMyRooms } from "../apis/room";

import CreateRoomForm from "../components/roomlist/CreateRoomForm";
import RoomCardList from "../components/roomlist/RoomCardList";


export default function MainPage() {
    const navigate = useNavigate();
    const [rooms, setRooms] = useState([]);

    const user = useSelector((state) => state.auth.user);
    const nickname = user?.nickname;

    useEffect(() => {
        const token = localStorage.getItem("accessToken");
        console.log(token);
        if (!token) {
            navigate("/login");
            return;
        }

        const fetch = async () => {
            try {
                const res = await getMyRooms();
                setRooms(res.data.data);
                console.log(res.data.data);
            } catch (err) {
                console.error("방 조회 실패: ", err);
            }
        };

        fetch();
    }, []);

    return (
        <div className="space-y-5">
            <section>
                <h1 className="text-2xl font-bold">안녕하세요 {nickname}님 👋</h1>
                <p className="text-gray-600 mt-1">스터디를 시작하거나 참여해보세요!</p>
            </section>

            <CreateRoomForm />

            <RoomCardList rooms={rooms} />
        </div>
    )
}