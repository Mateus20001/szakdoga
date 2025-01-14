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
      menuName: "Általános",
      menuDropdownList: [
        {
          name: "Hírek",
          link: "/news",
          roles: []
        },
        {
          name: "Szakokról",
          link: "/aboutMajors",
          roles: []
        },
        {
          name: "Karokról",
          link: "/aboutFaculties",
          roles: []
        }
      ]
    },
    {
        menuName: "Saját adatok",
        menuDropdownList: [
          {
            name: "Személyes adatok",
            link: "/personaldata",
            roles: [] 
          },
          {
            name: "Elérhetőségek",
            link: "/contacts",
            roles: [] 
          },
          {
            name: "Képzettségek",
            link: "/idk",
            roles: [] 
          },
          {
            name: "Adatmódosítás",
            link: "/profilechange",
            roles: [] 
          }
        ]
      },
      {
        menuName: "Tanulmányok",
        menuDropdownList: [
          {
            name: "Képzés adatok",
            link: "/dashboard",
            roles: ["STUDENT"] 
          },
          { 
            name: "Kurzus felvétele",
            link: "/courseadmission",
            roles: ["STUDENT"]
          },
          { 
            name: "Órarendtervező",
            link: "/timetablemaker",
            roles: ["STUDENT"]
          },
          { 
            name: "Felvett kurzusok",
            link: "/coursestaken",
            roles: ["STUDENT"]
          },
          { 
            name: "Vizsgajelentkezés",
            link: "/examadmission",
            roles: ["STUDENT"]
          },
          { 
            name: "Felvett vizsgák",
            link: "/examstaken",
            roles: ["STUDENT"]
          }
        ]
      },
      {
        menuName: "Admin Funkciók",
        menuDropdownList: [
          {
            name: "Felhasználó hozzáadása",
            link: "/addProfile",
            roles: ["ADMIN"]
          }
        ]
      }
    ];