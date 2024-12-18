import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('pro-user', () => {
    const user = ref(null)
    const setUser =(newUser)=>{
        user.value=newUser
    }
    const removeUser = () => {
       localStorage.removeItem('pro-user')
    }

    return { user, removeUser, setUser }
},{
    persist:true
})
