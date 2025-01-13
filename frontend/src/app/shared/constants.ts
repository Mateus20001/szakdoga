export interface MenuItem {
    name: string;
    link: string;
    roles: string[];
}
  
export interface MenuDropdown {
    menuName: string;
    menuDropdownList: MenuItem[];
}
export const MenuDropdownMenuObjects: MenuDropdown[] = [
    {
        menuName: "Saját adatok",
        menuDropdownList: [
          {
            name: "Személyes adatok",
            link: "/personaldata",
            roles: [] // Visible to all roles
          },
          {
            name: "Elérhetőségek",
            link: "/contacts",
            roles: [] // Visible to all roles
          },
          {
            name: "Képzettségek",
            link: "/idk",
            roles: ["USER", "ADMIN"] // Visible to USER and ADMIN roles only
          },
          {
            name: "Adatmódosítás",
            link: "/profilechange",
            roles: [] // Visible to all roles
          }
        ]
      },
      {
        menuName: "Tanulmányok",
        menuDropdownList: [
          {
            name: "Képzés adatok",
            link: "/dashboard",
            roles: ["STUDENT"] // Visible to ADMIN role only
          }
        ]
      }
    ];