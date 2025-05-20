import { Link, useSearchParams } from "react-router";
import Button from "../components/common/Button";
import Input from "../components/common/Input";
import { useState } from "react";

export default function LoginPage() {
    const [form, setForm] = useState({
        nickname: "",
        password: "",
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (form.nickname.length < 1 || form.password < 1) {
            alert("로그인 정보를 입력해주세요.");
            return;
        }

        console.log("로그인 요청: ", form);
    }


    return (
        <>
            <h2 className="text-3xl font-bold mb-2 text-center">로그인</h2>
            <p className="text-center text-gray-500 mb-6">
                계정을 입력하고 스터디에 참여해보세요.
            </p>
            <form className="space-y-4" onSubmit={handleSubmit}>
                <Input type="text" name="nickname" value={form.username} placeholder="닉네임을 입력해주세요" onChange={handleChange}/>
                <Input type="password" name="password" value={form.password} placeholder="비밀번호를 입력해주세요" onChange={handleChange}/>
                <div className="flex justify-end ">
                    <Button type="submit">로그인</Button>
                </div>
            </form>

            <p className="mt-4 text-sm text-center text-gray-500">
                계정이 없으신가요? {" "}
                <Link to="/register" className="text-blue-500 hover:underline">회원가입</Link>
            </p>
        </>
    )
}