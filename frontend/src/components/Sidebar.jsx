import { Link } from "react-router";
import { useSelector } from "react-redux";

export default function Sidebar() {
    const user = useSelector((state) => state.auth.user);
    const nickname = user?.nickname;

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
                    <li><button className="w-full text-left hover:text-green-300">백엔드 스터디 방</button></li>
                    <li><button className="w-full text-left hover:text-green-300">정보처리기사 스터디 방</button></li>
                    <li><button className="w-full text-left hover:text-green-300">토익 스터디 방</button></li>
                </ul>
            </nav>


            <div className="space-y-2 mt-6">
                <hr className="border-green-600" />
                <Link to="/mypage" className="block hover:text-green-300">마이페이지</Link>
                <button className="hover:text-red-400">로그아웃</button>
            </div>
        </nav>
    )
}