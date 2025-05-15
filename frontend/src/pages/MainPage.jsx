import { useState } from "react"
import CreateRoomForm from "../components/CreateRoomForm";
import RoomCard from "../components/RoomCardList";
import RoomCardList from "../components/RoomCardList";


export default function MainPage() {
    const dummyRooms = [
        { id: 1, title: "React 집중 스터디", participants: 3, capacity: 6},
        { id: 2, title: "백엔드 스터디 방", participants: 2, capacity: 4},
        { id: 3, title: "정보처리기사 스터디 방", participants: 1, capacity: 3},
        { id: 4, title: "토익 스터디 방", participants: 3, capacity: 5},
        { id: 5, title: "심심풀이 땅콩 방", participants: 1, capacity: 2},
    ]

    const nickname = "재석군"

    return (
        <div className="space-y-5">
            <section>
                <h1 className="text-2xl font-bold">안녕하세요 {nickname}님 👋</h1>
                <p className="text-gray-600 mt-1">스터디를 시작하거나 참여해보세요!</p>
            </section>

            <CreateRoomForm />

            <RoomCardList rooms={dummyRooms} />
        </div>
    )
}