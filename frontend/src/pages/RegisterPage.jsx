import { Link } from "react-router";
import Button from "../components/common/Button";
import Input from "../components/common/Input";
import { checkNicknameApi } from "../apis/user";
import { registerApi } from "../apis/auth";
import { useState } from "react";
import { useNavigate } from "react-router";

export default function RegisterPage() {
    const [form, setForm] = useState({
        nickname: "",
        password: "",
        confirmPassword: "",
    });

    const [nicknameMsg, setNicknameMsg] = useState("");
    const [nicknameChecked, setNicknameChecked] = useState(false);

    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({
            ...prev,
            [name]: value,
        }));

        if (name === "nickname") {
            setNicknameChecked(false);
            setNicknameMsg("");
        }
    };

    const handleNicknameCheck = async () => {
        if (!form.nickname.trim()) {
            setNicknameMsg("닉네임을 입력해주세요");
            setNicknameChecked(false);
            return;
        }

        try {
            const res = await checkNicknameApi(form.nickname);
            if (res.data.success) {
                setNicknameMsg("사용 가능한 닉네임입니다.");
                setNicknameChecked(true);
            }
        } catch (err) {
            setNicknameMsg("이미 사용 중인 닉네임입니다.");
            setNicknameChecked(false);
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!nicknameChecked) {
            alert("닉네임 중복 확인을 해주세요.");
            return;
        }

        if (form.nickname.length < 2 || form.nickname.length > 8) {
            alert("닉네임은 2글자 이상 8글자 이하로 해주세요.");
            return;
        }

        if (!nicknameChecked) {
            alert("닉네임 중복 확인을 해주세요.");
            return;
        }

        if (form.password !== form.confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        if (form.password.length < 4 || form.password.length > 15) {
            alert("비밀번호는 4글자 이상 15글자 이하로 해주세요.");
            return;
        }

        try {
            await registerApi(form.nickname, form.password);
            alert("회원가입 성공! 로그인 페이지로 이동합니다.");
            navigate("/login");
        } catch (error) {
            alert("회원가입 실패");
        }
    }

    return (
        <>
            <h2 className="text-3xl font-bold mb-2 text-center">회원가입</h2>
            <p className="text-center text-gray-500 mb-6">
                한 단계 성장하기까지 단 1분! <br />
                간단하게 가입하고 함께 공부해요.
            </p>
            <form className="space-y-4" onSubmit={handleSubmit}>
                <div className="flex gap-2 ">
                    <Input type="text" name="nickname" value={form.nickname} onChange={handleChange} placeholder="닉네임을 입력해주세요" className="flex-[6]"
                        error={!nicknameChecked && nicknameMsg} success={nicknameChecked && nicknameMsg}/>
                    <Button type="button" size="sm" variant="create" className="flex-[1] h-[42px]" onClick={handleNicknameCheck}>중복확인</Button>
                </div>
                <Input type="password" name="password" value={form.password} onChange={handleChange} placeholder="비밀번호를 입력해주세요"/>
                <Input type="password" name="confirmPassword" value={form.confirmPassword} onChange={handleChange} placeholder="비밀번호를 한번 더 입력해주세요"/>
                <div className="flex justify-end">
                    <Button type="submit" variant="join">회원가입</Button>
                </div>
            </form>
            
            <p className="mt-4 text-sm text-center text-gray-500">
                이미 계정이 있으신가요? {" "}
                <Link to="/login" className="text-blue-500 hover:underline">로그인</Link>
            </p>
        </>
    )
}