import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router'
import './index.css'
import AuthLayout from './layouts/AuthLayout.jsx'
import LoginPage from './pages/LoginPage.jsx'
import RegisterPage from './pages/RegisterPage.jsx'
import MainLayout from './layouts/MainLayout.jsx'
import MainPage from './pages/MainPage.jsx'
import ChatRoomLayout from './layouts/ChatRoomLayout.jsx'
import ChatRoomPage from './pages/ChatRoomPage.jsx'
import MyPage from './pages/MyPage.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>

        <Route element={<MainLayout />}>
          <Route index element={<MainPage />} />
          <Route path="mypage" element={<MyPage />} />
        </Route>

        <Route element={<AuthLayout />}>
          <Route path="login" element={<LoginPage />} />
          <Route path="register" element={<RegisterPage />} />
        </Route>

        <Route path="chatroom/:roomId" element={<ChatRoomLayout />}>
          <Route index element={<ChatRoomPage />} />
        </Route>

      </Routes>
    </BrowserRouter>
  </StrictMode>,
)
