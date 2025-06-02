import { Link, useNavigate } from "react-router";
import { useSelector } from "react-redux";
import { useEffect, useState } from "react";
import { getMyRoomsRecent } from "../apis/room";
import { logoutApi } from "../apis/user";

export default function Sidebar() {
    const user = useSelector((state) => state.auth.user);
    const nickname = user?.nickname;

    const navigate = useNavigate();

    const [recentRooms, setRecentRooms] = useState([]);

    useEffect(() => {
        const fetch = async () => {
            try {
                const res = await getMyRoomsRecent();
                setRecentRooms(res.data.data);
                console.log(res.data.data);
            } catch (err) {
                console.error("최근 들어간 방 조회 실패: ", err);
            }
        };

        fetch();
    }, []);

    const handleLogout = () => {
        const logout = async () => {
            try {
                await logoutApi();
                localStorage.removeItem("accessToken");
                navigate("/login");
            } catch (err) {
                console.error("로그아웃 요청 실패", err);
            }
        }

        logout();
    }

    return (
        <nav className="w-64 h-screen bg-green-800 text-white flex flex-col p-4 fixed left-0 top-0">
            <Link to="/" className="text-xl font-bold mb-6 hover:text-green-300">거북이 달린다</Link>

            <div className="mb-4">
                <p className="text-sm text-green-200">닉네임</p>
                <p className="font-semibold">{nickname}</p>
            </div>

            <nav className="flex-1">
                <ul className="space-y-2">
                    <li className="text-sm text-green-200 mt-4">최근에 입장한 방</li>
                    {recentRooms.length === 0 ? (
                        <li className="text-sm text-gray-300">참여 중인 방이 없습니다.</li>
                    ): (
                        recentRooms.map(room => (
                            <li key={room.roomId}>
                                <Link
                                    to={`/chatroom/${room.roomId}`}
                                    className="w-full text-left hover:text-green-300"
                                >
                                    {room.title}
                                </Link>
                            </li>
                        ))
                    )}
                </ul>
            </nav>


            <div className="space-y-2 mt-6">
                <hr className="border-green-600" />
                <Link to="/mypage" className="block hover:text-green-300">마이페이지</Link>
                <button className="hover:text-red-400" onClick={handleLogout}>로그아웃</button>
            </div>
        </nav>
    )
}