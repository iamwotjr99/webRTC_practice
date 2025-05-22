import { Routes, Route } from 'react-router';
import MainLayout from '../layouts/MainLayout';
import AuthLayout from '../layouts/AuthLayout';
import ChatRoomLayout from '../layouts/ChatRoomLayout';
import ProtectedRoute from './ProtectedRoute';
import MainPage from '../pages/MainPage';
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import MyPage from '../pages/MyPage';
import ChatRoomPage from '../pages/ChatRoomPage';
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';  
import { login } from '../store/authSlice';
import { getUserInfo } from '../apis/user';

export default function AppRouter() {
    const dispatch = useDispatch();

    useEffect(() => {
        const token = localStorage.getItem("accessToken");

        if (token) {
            dispatch(login({ token, user: null}));
            getUserInfo()
                .then((res) => {
                    dispatch(login({
                        token,
                        user: {
                            userId: res.data.data.userId,
                            nickname: res.data.data.nickname,
                        }
                    }));
                })
                .catch(() => {
                    console.error("토큰 복원 실패");
                    localStorage.removeItem("accessToken");
                });
        }
    }, []);

    return (
        <Routes>

          <Route element={<MainLayout />}>
            <Route index element={<MainPage />} />
            <Route path="mypage" element={
                <ProtectedRoute>
                  <MyPage />
                </ProtectedRoute>
              } 
            />
          </Route>

          <Route element={<AuthLayout />}>
            <Route path="login" element={<LoginPage />} />
            <Route path="register" element={<RegisterPage />} />
          </Route>

          <Route path="chatroom/:roomId" element={<ChatRoomLayout />}>
            <Route index element={<ChatRoomPage />} />
          </Route>

        </Routes>
    )
}